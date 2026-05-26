package edu.uit.se122.server.identity;

import edu.uit.se122.server.identity.internal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/member")
@RequiredArgsConstructor
public class MemberAdminController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<MemberContract.Res>> getAll() {
        return ResponseEntity.ok(memberService.getAll());
    }
}
