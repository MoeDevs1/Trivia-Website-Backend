package com.muslimtrivia.Trivia.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopUserDTO {
    private String username;
    private int score;
    private String flag;

}
