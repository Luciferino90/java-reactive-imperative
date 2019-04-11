package it.arubapec.javareactive.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response implements Serializable {

    private static final long serialVersionUID = 369935215244340009L;

    private String response;

}
