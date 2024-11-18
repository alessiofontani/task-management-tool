package com.alessiofontani.taskmanagementtool.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse extends BasicResponse {

    private String jwt;

}
