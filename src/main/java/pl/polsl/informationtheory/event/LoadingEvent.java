package pl.polsl.informationtheory.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoadingEvent {
    private final boolean isSuccess;
}
