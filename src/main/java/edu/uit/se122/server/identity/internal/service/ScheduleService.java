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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final AdministratorRepository administratorRepository;

    public List<ScheduleContract.Res> getAll() {
        return scheduleRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public void create(ScheduleContract.Req dto) {
        if (dto.adminIds() == null || dto.adminIds().isEmpty()) {
            throw new RuntimeException("Admin list must not be empty");
        }

        List<Administrator> admins = administratorRepository.findAllById(dto.adminIds());

        if (admins.size() != dto.adminIds().size()) {
            throw new RuntimeException("One or more admins not found");
        }

        Schedule schedule = new Schedule();
        schedule.setWorkDate(dto.workDate());
        schedule.setDayOfWeek(dto.dayOfWeek());
        schedule.setFromTime(dto.fromTime());
        schedule.setToTime(dto.toTime());

        List<AdminSchedule> adminSchedules = admins.stream()
                .map(admin -> {
                    AdminSchedule adminSchedule = new AdminSchedule();
                    adminSchedule.setAdmin(admin);
                    adminSchedule.setSchedule(schedule);
                    return adminSchedule;
                })
                .toList();

        schedule.setAdminSchedules(adminSchedules);

        scheduleRepository.save(schedule);
    }

    private ScheduleContract.Res mapToDTO(Schedule entity) {
        List<ScheduleContract.AdminRes> admins = entity.getAdminSchedules().stream()
                .map(adminSchedule -> {
                    Administrator admin = adminSchedule.getAdmin();

                    return new ScheduleContract.AdminRes(
                            admin.getAdminId(),
                            admin.getName()
                    );
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
}
