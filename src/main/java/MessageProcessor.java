import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.Collections;

@ApplicationScoped
public class MessageProcessor {

    static final int BATCH_SIZE = 17;

    @Outgoing("source")
    Multi<String> generate() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                    .flatMap(v -> Multi.createFrom().iterable(Collections.nCopies(BATCH_SIZE, Long.toString(v))));
    }

    @Incoming("events")
    @Outgoing("sink")
    Multi<String> process(Multi<String> source) {
        // We need a reactive stream transformer, i.e., something with the following signature
        // Flow.Publisher<Message<O>> method(Flow.Publisher<Message<I>> publisher)
        // It may be a noop, though.
        return source;
    }

    @Incoming("sink")
    void process(String event) {
        System.out.println(event);
    }

}
