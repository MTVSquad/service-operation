package com.vsquad.iroas.service;

import com.vsquad.iroas.aggregate.dto.request.ReqRankingDto;
import com.vsquad.iroas.aggregate.dto.response.ResRankingDto;
import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.entity.Ranking;
import com.vsquad.iroas.aggregate.vo.PlayTime;
import com.vsquad.iroas.config.exception.PlayerNotFoundException;
import com.vsquad.iroas.config.exception.RankingNotFoundException;
import com.vsquad.iroas.config.token.PlayerPrincipal;
import com.vsquad.iroas.repository.PlayerRepository;
import com.vsquad.iroas.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;

    private final PlayerRepository playerRepository;

    @Transactional
    public ResRankingDto addRanking(ReqRankingDto dto) {

        boolean isClear = dto.getClearYn();
        LocalDateTime playStartTime = dto.getPlayStartTime();
        LocalDateTime playClearTime = dto.getPlayClearTime();

        PlayerPrincipal userDetails = (PlayerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long playerId = userDetails.getId();
        Long creatorMapId = dto.getCreatorMapId();
        Long playElapsedTime = dto.getElapsedTime();

        // 회원 찾기
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("회원이 존재하지 않습니다."));

        // 랭킹이 있는지 확인
        Optional<Ranking> isRaking = rankingRepository.findByPlayerAndCreatorMapId(player, creatorMapId);

        // 랭킹 결과를 저장할 변수
        final Ranking rankingResult;

        if (isClear) {
            // 클리어한 경우
            rankingResult = isRaking.map(foundRanking -> {
                PlayTime playTime = new PlayTime(playStartTime, playClearTime, playElapsedTime);

                if (playTime.getPlayMilliSecond() < foundRanking.getPlayTime().getPlayMilliSecond()) {
                    // 기존 랭킹 업데이트
                    foundRanking.setPlayTime(playTime);
                }
                foundRanking.setPlayCount(foundRanking.getPlayCount() + 1);
                foundRanking.setClearCount(foundRanking.getClearCount() + 1);
                return foundRanking;
            }).orElseGet(() ->
                    rankingRepository.save(
                            new Ranking(player, creatorMapId, new PlayTime(playStartTime, playClearTime, playElapsedTime), 1, 1)
                    )
            );
        } else {
            // 클리어하지 않은 경우
            rankingResult = isRaking.map(foundRanking -> {
                foundRanking.setPlayCount(foundRanking.getPlayCount() + 1);
                return foundRanking;
            }).orElseGet(() ->
                    rankingRepository.save(
                            new Ranking(player, creatorMapId, new PlayTime(playStartTime, playClearTime, playElapsedTime), 1, 0)
                    )
            );
        }

        return ResRankingDto.convertToDto(rankingResult, player);
    }

    public Page<ResRankingDto> getRanking(Long creatorMapId, Pageable pageable) {

        Page<Ranking> ranking = rankingRepository.findByCreatorMapIdAndClearCountIsNot(creatorMapId, 0, pageable);

        if(ranking.isEmpty()) {
            throw new RankingNotFoundException();
        }

        List<ResRankingDto> rankingList = ranking.stream()
                .map((rank) -> ResRankingDto.convertToDto(rank, rank.getPlayer()))
                .collect(Collectors.toList());

        return new PageImpl<>(rankingList);
    }
}
