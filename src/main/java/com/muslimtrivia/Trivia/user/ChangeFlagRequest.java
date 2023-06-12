package com.muslimtrivia.Trivia.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeFlagRequest {
    private String userName;
    private String newFlag;

    // getters and setters
}
