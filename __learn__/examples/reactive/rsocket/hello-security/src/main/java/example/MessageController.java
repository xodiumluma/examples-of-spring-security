package example;

import reactor.core.publisher.Mono;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {
  
  @MessageMapping("message")
  public Mono<String> message() {
    return Mono.just("Hello");
  }
}
