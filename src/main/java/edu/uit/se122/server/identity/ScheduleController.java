package edu.uit.se122.server.identity;

import edu.uit.se122.server.identity.internal.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<ScheduleContract.Res>> getAll() {
        return ResponseEntity.ok(scheduleService.getAll());
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ScheduleContract.Req dto) {
        scheduleService.create(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }
}
