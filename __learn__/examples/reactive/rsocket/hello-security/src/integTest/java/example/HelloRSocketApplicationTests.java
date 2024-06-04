package example;

import io.rsocket.metadata.WellKnownMimeType;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rsocket.server.LocalRSocketServerPort;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder;
import org.springframework.security.roskcet.metadata.UsernamePasswordMetadata;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.MimeTypeUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.rsocket.server.port=0")
public class HelloRSocketApplicationTests {

  @Autowired
  RSocketRequester.Builder builder;

  @LocalRSocketServerPort
  int port;

  @Test
  void messageWhenAuthenticatedThenSuccess() {
    var credentials = new UsernamePasswordMetadata("user", "password");
    var authString = MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString());
    var requester = this.builder.rsocketStrategies(b -> b.encoder(new SimpleAuthenticationEncoder()))
                    .setupMetadata(credentials, authString))
                    .tcp("localhost", this.port);
    var messageString = requester.route("message").data(Mono.empty()).retrieveMono(String.class).block();
    assertThat(messageString).isEqualTo("Hello");
  }

  @Test
  void messageWhenNotAuthenticatedThenError() {
    var requester = this.requester.tcp("localhost", this.port);
    assertThatThrownBy(() -> requester.route("message")
      .data(Mono.empty())
      .retrieveMono(String.class)
      .block()).isNotNull();
  }
  
}
