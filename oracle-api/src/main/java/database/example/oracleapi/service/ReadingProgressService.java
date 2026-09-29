package database.example.oracleapi.service;

import database.example.oracleapi.entity.ReadingProgress;
import database.example.oracleapi.repository.ReadingProgressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadingProgressService {

    private final ReadingProgressRepository repository;

    public ReadingProgressService(
            ReadingProgressRepository repository
    ) {
        this.repository = repository;
    }


    public List<ReadingProgress> getByMember(
            Long memberId
    ) {
        return repository.findByMemberId(memberId);
    }


    public ReadingProgress getProgress(
            Long memberId,
            Long bookId
    ) {
        return repository
                .findByMemberIdAndBookId(memberId, bookId)
                .orElseGet(() -> {

                    ReadingProgress progress =
                            new ReadingProgress();

                    progress.setMemberId(memberId);
                    progress.setBookId(bookId);
                    progress.setProgress(0);
                    progress.setStatus("UNREAD");

                    return progress;
                });
    }


    public ReadingProgress updateProgress(
            Long memberId,
            Long bookId,
            Integer value
    ) {

        int progressValue =
                Math.max(0, Math.min(value, 100));

        ReadingProgress progress =
                repository
                        .findByMemberIdAndBookId(
                                memberId,
                                bookId
                        )
                        .orElseGet(() -> {

                            ReadingProgress newProgress =
                                    new ReadingProgress();

                            newProgress.setMemberId(memberId);
                            newProgress.setBookId(bookId);

                            return newProgress;
                        });


        progress.setProgress(progressValue);


        if (progressValue == 0) {

            progress.setStatus("UNREAD");

        } else if (progressValue >= 100) {

            progress.setStatus("READ");

        } else {

            progress.setStatus("READING");
        }


        return repository.save(progress);
    }


    public ReadingProgress markAsRead(
            Long memberId,
            Long bookId
    ) {

        return updateProgress(
                memberId,
                bookId,
                100
        );
    }
}