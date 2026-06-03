FROM rabbitmq:4.2-management

USER root

RUN apt-get update && \
    apt-get install -y wget && \
    wget -O /plugins/rabbitmq_delayed_message_exchange-4.2.0.ez \
    https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases/download/v4.2.0/rabbitmq_delayed_message_exchange-4.2.0.ez && \
    rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange && \
    apt-get clean

USER rabbitmq