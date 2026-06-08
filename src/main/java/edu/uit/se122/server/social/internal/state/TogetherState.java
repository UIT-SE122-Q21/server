package edu.uit.se122.server.social.internal.state;

import edu.uit.se122.server.social.internal.entity.ChatDetail;
import edu.uit.se122.server.social.internal.entity.MemberCache;
import edu.uit.se122.server.social.internal.entity.Together;

public interface TogetherState {
    void joinTogether(Together together, MemberCache member);
    void checkOutOrder(Together together);
    void cancelTogether(Together together);
    ChatDetail saveChatMessage(Together together, Integer memberId, String message);
    ChatDetail joinChat(Together together, Integer memberId, String message);
    ChatDetail leaveChat(Together together, Integer memberId, String message);
}
