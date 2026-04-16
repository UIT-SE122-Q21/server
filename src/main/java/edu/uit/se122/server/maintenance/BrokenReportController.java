package edu.uit.se122.server.maintenance;

import edu.uit.se122.server.maintenance.internal.service.BrokenReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/broken-report")
@RequiredArgsConstructor
public class BrokenReportController {

    private final BrokenReportService brokenReportService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> create(@ModelAttribute BrokenReportContract.Request dto) throws IOException {
        brokenReportService.create(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }

    @GetMapping
    public ResponseEntity<List<BrokenReportContract.Response>> getAll() {
        return ResponseEntity.ok(brokenReportService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrokenReportContract.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(brokenReportService.getById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> update(
            @PathVariable Integer id,
            @ModelAttribute BrokenReportContract.Request dto) throws IOException {
        brokenReportService.update(id, dto);
        return ResponseEntity.ok(Map.of("message", "Chỉnh sửa thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        brokenReportService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
    }
}
