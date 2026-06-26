package in.elcot.avgcxr.common.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@org.springframework.context.annotation.Profile("!no-rabbit")
public class RabbitMQConfig {

  public static final String EXCHANGE = "avgcxr.events";
  public static final String DLX_EXCHANGE = "avgcxr.dlx";

  // Queue names — must match infra/docker/rabbitmq/definitions.json + worker listeners
  public static final String QUEUE_APPLICATION_SUBMITTED = "avgc.application.submitted";
  public static final String QUEUE_APPLICATION_APPROVED = "avgc.application.approved";
  public static final String QUEUE_APPLICATION_REJECTED = "avgc.application.rejected";
  public static final String QUEUE_NOTIFICATION_DISPATCH = "avgc.notification.dispatch";
  public static final String QUEUE_NOTIFICATION_EMAIL = "avgcxr.notification.email";
  public static final String QUEUE_NOTIFICATION_SMS = "avgcxr.notification.sms";
  public static final String QUEUE_SEARCH_INDEX = "avgc.search.index";
  public static final String QUEUE_AUDIT_CREATED = "avgc.audit.created";
  public static final String QUEUE_USER_REGISTERED = "avgc.user.registered";
  public static final String QUEUE_EXPORT_GENERATE = "avgcxr.export.generate";
  public static final String QUEUE_DEAD = "avgcxr.dead";

  // Routing keys (topic exchange)
  public static final String RK_APPLICATION_SUBMITTED = "application.submitted";
  public static final String RK_APPLICATION_APPROVED = "application.approved";
  public static final String RK_APPLICATION_REJECTED = "application.rejected";
  public static final String RK_NOTIFICATION_DISPATCH = "notification.dispatch";
  public static final String RK_NOTIFICATION_EMAIL = "notification.email.*";
  public static final String RK_NOTIFICATION_SMS = "notification.sms.*";
  public static final String RK_SEARCH_INDEX = "search.index";
  public static final String RK_AUDIT_CREATED = "audit.created";
  public static final String RK_USER_REGISTERED = "user.registered";
  public static final String RK_EXPORT_GENERATE = "export.generate.*";

  @Bean
  public TopicExchange eventsExchange() {
    return new TopicExchange(EXCHANGE, true, false);
  }

  @Bean
  public TopicExchange deadLetterExchange() {
    return new TopicExchange(DLX_EXCHANGE, true, false);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(jsonMessageConverter());
    template.setChannelTransacted(true);
    return template;
  }

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
      ConnectionFactory connectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(jsonMessageConverter());
    factory.setPrefetchCount(10);
    return factory;
  }

  private Queue durableQueue(String name) {
    return QueueBuilder.durable(name)
        .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
        .withArgument("x-dead-letter-routing-key", name + ".dlq")
        .build();
  }

  @Bean
  public Queue applicationSubmittedQueue() {
    return durableQueue(QUEUE_APPLICATION_SUBMITTED);
  }

  @Bean
  public Queue applicationApprovedQueue() {
    return durableQueue(QUEUE_APPLICATION_APPROVED);
  }

  @Bean
  public Queue applicationRejectedQueue() {
    return durableQueue(QUEUE_APPLICATION_REJECTED);
  }

  @Bean
  public Queue notificationDispatchQueue() {
    return durableQueue(QUEUE_NOTIFICATION_DISPATCH);
  }

  @Bean
  public Queue notificationEmailQueue() {
    return durableQueue(QUEUE_NOTIFICATION_EMAIL);
  }

  @Bean
  public Queue notificationSmsQueue() {
    return durableQueue(QUEUE_NOTIFICATION_SMS);
  }

  @Bean
  public Queue searchIndexQueue() {
    return durableQueue(QUEUE_SEARCH_INDEX);
  }

  @Bean
  public Queue auditCreatedQueue() {
    return durableQueue(QUEUE_AUDIT_CREATED);
  }

  @Bean
  public Queue userRegisteredQueue() {
    return durableQueue(QUEUE_USER_REGISTERED);
  }

  @Bean
  public Queue exportGenerateQueue() {
    return durableQueue(QUEUE_EXPORT_GENERATE);
  }

  @Bean
  public Queue deadQueue() {
    return durableQueue(QUEUE_DEAD);
  }

  @Bean
  public Binding applicationSubmittedBinding() {
    return BindingBuilder.bind(applicationSubmittedQueue())
        .to(eventsExchange())
        .with(RK_APPLICATION_SUBMITTED);
  }

  @Bean
  public Binding applicationApprovedBinding() {
    return BindingBuilder.bind(applicationApprovedQueue())
        .to(eventsExchange())
        .with(RK_APPLICATION_APPROVED);
  }

  @Bean
  public Binding applicationRejectedBinding() {
    return BindingBuilder.bind(applicationRejectedQueue())
        .to(eventsExchange())
        .with(RK_APPLICATION_REJECTED);
  }

  @Bean
  public Binding notificationDispatchBinding() {
    return BindingBuilder.bind(notificationDispatchQueue())
        .to(eventsExchange())
        .with(RK_NOTIFICATION_DISPATCH);
  }

  @Bean
  public Binding notificationEmailBinding() {
    return BindingBuilder.bind(notificationEmailQueue())
        .to(eventsExchange())
        .with(RK_NOTIFICATION_EMAIL);
  }

  @Bean
  public Binding notificationSmsBinding() {
    return BindingBuilder.bind(notificationSmsQueue())
        .to(eventsExchange())
        .with(RK_NOTIFICATION_SMS);
  }

  @Bean
  public Binding searchIndexBinding() {
    return BindingBuilder.bind(searchIndexQueue()).to(eventsExchange()).with(RK_SEARCH_INDEX);
  }

  @Bean
  public Binding auditCreatedBinding() {
    return BindingBuilder.bind(auditCreatedQueue()).to(eventsExchange()).with(RK_AUDIT_CREATED);
  }

  @Bean
  public Binding userRegisteredBinding() {
    return BindingBuilder.bind(userRegisteredQueue()).to(eventsExchange()).with(RK_USER_REGISTERED);
  }

  @Bean
  public Binding exportGenerateBinding() {
    return BindingBuilder.bind(exportGenerateQueue()).to(eventsExchange()).with(RK_EXPORT_GENERATE);
  }

  /**
   * Per-entity event queues consumed by the auto-generated *EventConsumer listeners. They are
   * declared here so RabbitAdmin creates them on startup; otherwise each listener's passive
   * declaration fails with 404 NOT_FOUND. Queues already declared above are excluded.
   */
  @Bean
  public Declarables stubEventQueues() {
    String[] names = {
      "avgc.audit.updated",
      "avgc.auth.created",
      "avgc.auth.updated",
      "avgc.businessconnect.created",
      "avgc.businessconnect.updated",
      "avgc.document.submitted",
      "avgc.file.created",
      "avgc.freelancerregistry.created",
      "avgc.freelancerregistry.updated",
      "avgc.helpdesk.created",
      "avgc.helpdesk.updated",
      "avgc.notification.updated",
      "avgc.scheme.approved",
      "avgc.scheme.submitted",
      "avgc.search.created",
      "avgc.search.updated",
      "avgc.talentconnect.created",
      "avgc.talentconnect.updated",
      "avgc.user.created",
      "avgc.user.updated",
      "avgc.workflow.created",
      "avgc.workflow.updated"
    };
    java.util.List<Declarable> queues = new java.util.ArrayList<>();
    for (String name : names) {
      queues.add(QueueBuilder.durable(name).build());
    }
    return new Declarables(queues);
  }
}
