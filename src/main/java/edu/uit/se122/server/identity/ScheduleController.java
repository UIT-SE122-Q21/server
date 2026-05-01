package edu.uit.se122.server.identity;

import edu.uit.se122.server.identity.internal.service.ScheduleService;
import edu.uit.se122.server.inventory.ProductCategoryContract;
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
    public ResponseEntity<Object> create(@RequestBody ScheduleContract.CreateReq dto) {
        scheduleService.create(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody ScheduleContract.UpdateReq dto) {
        scheduleService.update(id, dto);
        return ResponseEntity.ok(Map.of("message", "Chỉnh sửa thành công"));
    }
}
