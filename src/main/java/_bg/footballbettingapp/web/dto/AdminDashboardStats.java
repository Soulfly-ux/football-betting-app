package _bg.footballbettingapp.web.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AdminDashboardStats {


    private long totalUsers;
    private long activeUsers;
    private long adminUsers;


    private long totalBets;
    private long pendingBets;
    private long wonBets;
    private long lostBets;
    private long cancelledBets;


    private long totalMatches;
    private long inProgressMatches;
    private long finishedMatches;
    private long cancelledMatches;
    private long scheduledMatches;
    private long overdueMatches;



}
