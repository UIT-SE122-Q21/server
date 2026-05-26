package edu.uit.se122.server.identity;

import edu.uit.se122.server.identity.internal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/member")
@RequiredArgsConstructor
public class MemberCustomerController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberContract.Res> getById(Integer id) {
        return ResponseEntity.ok(memberService.getById(id));
    }
}
