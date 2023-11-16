package com.vsquad.iroas.service;

import com.vsquad.iroas.aggregate.dto.ResRankingDto;
import com.vsquad.iroas.aggregate.dto.request.ReqRankingDto;
import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.entity.Ranking;
import com.vsquad.iroas.aggregate.vo.PlayTime;
import com.vsquad.iroas.config.token.PlayerPrincipal;
import com.vsquad.iroas.repository.PlayerRepository;
import com.vsquad.iroas.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;

    private final PlayerRepository playerRepository;

    @Transactional
    public void addRanking(ReqRankingDto dto) {

        boolean isClear = dto.getClearYn();

        LocalDateTime playStartTime = dto.getPlayStartTime();
        LocalDateTime playClearTime = dto.getPlayClearTime();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {

            PlayerPrincipal userDetails = (PlayerPrincipal) authentication.getPrincipal();

            // UserDetails에서 사용자 정보 사용
            Long playerId = userDetails.getId();

            String creatorMapId = dto.getCreatorMapId();

            // 회원 찾기
            Player player = playerRepository.findById(playerId)
                    .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

            // 랭킹이 있는지 확인
            Optional<Ranking> isRaking = rankingRepository.findByPlayerAndCreatorMapId(player, dto.getCreatorMapId());

            if(isClear) {
                // 클리어한 경우
                isRaking
                        .ifPresentOrElse(
                                (foundRanking) -> {

                                    PlayTime playTime = new PlayTime(playStartTime, playClearTime);

                                    if(playTime.getPlayMilliSecond() > foundRanking.getPlayTime().getPlayMilliSecond()) {
                                        // 랭킹이 있으면 업데이트
                                        foundRanking.setPlayTime(new PlayTime(playStartTime, playClearTime));
                                        foundRanking.setPlayCount(foundRanking.getPlayCount() + 1);
                                        foundRanking.setClearCount(foundRanking.getClearCount() + 1);
                                    } else {
                                        foundRanking.setPlayCount(foundRanking.getPlayCount() + 1);
                                        foundRanking.setClearCount(foundRanking.getClearCount() + 1);
                                    }
                                },
                                () -> {
                                    // 랭킹이 없으면 추가
                                    Ranking ranking = new Ranking(player, creatorMapId, new PlayTime(playStartTime, playClearTime), 1, 1);
                                    rankingRepository.save(ranking);
                                }
                        );
            } else {
                // 랭킹이 있는지 확인
                isRaking
                        .ifPresentOrElse(
                                (foundRanking) -> {
                                    // 랭킹이 있으면 업데이트
                                    foundRanking.setPlayCount(foundRanking.getPlayCount() + 1);
                                },
                                () -> {
                                    // 랭킹이 없으면 추가
                                    Ranking ranking = new Ranking(player, creatorMapId, new PlayTime(playStartTime, playClearTime), 1, 0);
                                    rankingRepository.save(ranking);
                                }
                        );
            }
        }
    }

    public Page<ResRankingDto> getRanking(String creatorMapId, Pageable pageable) {

        Page<Ranking> ranking = rankingRepository.findByCreatorMapIdAndClearCountIsNot(creatorMapId, 0, pageable);

        if(ranking.isEmpty() || ranking == null) {
            throw new NoSuchElementException("랭킹이 존재하지 않습니다.");
        }

        List<ResRankingDto> rankingList = ranking.stream()
                .map((rank) -> ResRankingDto.convertToDto(rank, rank.getPlayer()))
                .collect(Collectors.toList());

        return new PageImpl<>(rankingList);
    }
}
