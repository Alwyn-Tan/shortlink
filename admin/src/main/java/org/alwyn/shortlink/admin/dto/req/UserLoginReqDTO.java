package org.alwyn.shortlink.admin.dto.req;

import lombok.Data;

@Data
public class UserLoginReqDTO {
    String username;

    String password;
}
