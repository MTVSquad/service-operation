package com.vsquad.iroas.service;

import com.vsquad.iroas.aggregate.dto.ReqRankingDto;
import com.vsquad.iroas.aggregate.entity.Ranking;
import com.vsquad.iroas.aggregate.vo.PlayTime;
import com.vsquad.iroas.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;

    @Transactional
    public void addRanking(ReqRankingDto dto) {

        boolean isClear = dto.getClearYn();

        LocalDateTime playStartTime = dto.getPlayStartTime();
        LocalDateTime playClearTime = dto.getPlayClearTime();

        Long playerId = dto.getPlayerId();
        String creatorMapId = dto.getCreatorMapId();

        // 랭킹이 있는지 확인
        Optional<Ranking> isRaking = rankingRepository.findByPlayerIdAndCreatorMapId(dto.getPlayerId(), dto.getCreatorMapId());

        if(isClear) {

            Duration duration = Duration.between(playStartTime, playClearTime);
            long minutes = duration.toMinutes();

            isRaking
                .ifPresentOrElse(
                        (foundRanking) -> {

                            // 랭킹이 있으면 업데이트
                            foundRanking.setPlayTime(new PlayTime(playStartTime, playClearTime));
                            foundRanking.setPlayCount(foundRanking.getPlayCount() + 1);
                            foundRanking.setClearCount(foundRanking.getClearCount() + 1);
                        },
                        () -> {
                            // 랭킹이 없으면 추가
                            Ranking ranking = new Ranking(playerId, creatorMapId, new PlayTime(playStartTime, playClearTime), 1, 1);
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
                            Ranking ranking = new Ranking(playerId, creatorMapId, new PlayTime(playStartTime, playClearTime), 1, 0);
                            rankingRepository.save(ranking);
                        }
                );
        }
    }
}
