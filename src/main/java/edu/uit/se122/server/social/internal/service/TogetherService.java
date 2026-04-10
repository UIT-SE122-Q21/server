package edu.uit.se122.server.social.internal.service;

import edu.uit.se122.server.social.TogetherContract;
import edu.uit.se122.server.social.internal.entity.Together;
import edu.uit.se122.server.social.internal.repository.TogetherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TogetherService {
    private final TogetherRepository togetherRepository;

    public List<TogetherContract.Response> getAll() {
        return togetherRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public TogetherContract.Response getById(Integer id) {
        return togetherRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Together not found"));
    }

    public void create(TogetherContract.Request dto) {
        Together together = new Together();
        updateEntity(together, dto);
        Together saved = togetherRepository.save(together);
    }

    public void update(Integer id, TogetherContract.Request dto) {
        Together together = togetherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Together not found"));

        updateEntity(together, dto);
        togetherRepository.save(together);
    }

    public void delete(Integer id) { togetherRepository.deleteById(id); }

    private void updateEntity(Together entity, TogetherContract.Request dto) {
        entity.setStatus(dto.status());
        entity.setContent(dto.content());
        entity.setNumOfPlayers(dto.numOfPlayers());
        entity.setFromTime(dto.fromTime());
        entity.setToTime(dto.toTime());
    }

    private TogetherContract.Response mapToDTO(Together entity) {
        return new TogetherContract.Response(
                entity.getTogetherId(),
                entity.getStatus(),
                entity.getContent(),
                entity.getNumOfPlayers(),
                entity.getFromTime(),
                entity.getToTime()
        );
    }
}
