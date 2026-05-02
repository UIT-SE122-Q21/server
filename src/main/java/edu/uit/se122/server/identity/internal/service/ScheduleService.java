package edu.uit.se122.server.identity.internal.service;

import edu.uit.se122.server.identity.ScheduleContract;
import edu.uit.se122.server.identity.internal.entity.AdminSchedule;
import edu.uit.se122.server.identity.internal.entity.Administrator;
import edu.uit.se122.server.identity.internal.entity.Schedule;
import edu.uit.se122.server.identity.internal.repository.AdministratorRepository;
import edu.uit.se122.server.identity.internal.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final AdministratorRepository administratorRepository;

    public List<ScheduleContract.Res> getAll() {
        return scheduleRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public void create(ScheduleContract.CreateReq dto) {
        validateAdmins(dto.adminIds());
        List<Administrator> admins = administratorRepository.findAllById(dto.adminIds());

        Schedule schedule = new Schedule();
        schedule.setWorkDate(dto.workDate());
        schedule.setDayOfWeek(dto.dayOfWeek());
        schedule.setFromTime(dto.fromTime());
        schedule.setToTime(dto.toTime());

        List<AdminSchedule> adminSchedules = createAdminSchedules(admins, schedule);
        schedule.setAdminSchedules(adminSchedules);

        scheduleRepository.save(schedule);
    }

    public void update(Integer id, ScheduleContract.UpdateReq dto) {
        validateAdmins(dto.adminIds());
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        List<Administrator> admins = administratorRepository.findAllById(dto.adminIds());

        schedule.setFromTime(dto.fromTime());
        schedule.setToTime(dto.toTime());

        Set<Integer> requestedAdminIds = new HashSet<>(dto.adminIds());

        schedule.getAdminSchedules().removeIf(adminSchedule ->
                !requestedAdminIds.contains(adminSchedule.getAdmin().getAdminId())
        );

        Set<Integer> existingAdminIds = schedule.getAdminSchedules().stream()
                .map(adminSchedule -> adminSchedule.getAdmin().getAdminId())
                .collect(Collectors.toSet());

        admins.stream()
                .filter(admin -> !existingAdminIds.contains(admin.getAdminId()))
                .map(admin -> {
                    AdminSchedule adminSchedule = new AdminSchedule();
                    adminSchedule.setAdmin(admin);
                    adminSchedule.setSchedule(schedule);
                    return adminSchedule;
                })
                .forEach(schedule.getAdminSchedules()::add);
    }

    public void delete(Integer id) {
        scheduleRepository.deleteById(id);
    }

    private ScheduleContract.Res mapToDTO(Schedule entity) {
        List<Integer> admins = entity.getAdminSchedules().stream()
                .map(adminSchedule -> {
                    Administrator admin = adminSchedule.getAdmin();
                    return admin.getAdminId();
                })
                .toList();

        return new ScheduleContract.Res(
                entity.getScheduleId(),
                entity.getWorkDate(),
                entity.getDayOfWeek(),
                entity.getFromTime(),
                entity.getToTime(),
                admins
        );
    }

    private void validateAdmins(List<Integer> adminIds) {
        if (adminIds == null || adminIds.isEmpty()) {
            throw new RuntimeException("Admin list must not be empty");
        }

        List<Administrator> admins = administratorRepository.findAllById(adminIds);
        if (admins.size() != adminIds.size()) {
            throw new RuntimeException("One or more admins not found");
        }
    }

    private List<AdminSchedule> createAdminSchedules(List<Administrator> admins, Schedule schedule) {
        return admins.stream()
                .map(admin -> {
                    AdminSchedule adminSchedule = new AdminSchedule();
                    adminSchedule.setAdmin(admin);
                    adminSchedule.setSchedule(schedule);
                    return adminSchedule;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
