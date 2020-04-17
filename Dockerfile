FROM openjdk:14
RUN mkdir /opt/trust-bot \
    && chown 1001 /opt/trust-bot \
    && chmod "g+rwX" /opt/trust-bot \
    && chown 1001:root /opt/trust-bot

COPY build/distributions/trust-bot-1.0.0-SNAPSHOT.tar /opt/trust-bot
RUN tar -xvf /opt/trust-bot/trust-bot-1.0.0-SNAPSHOT.tar \
    && chown 1001 /opt/trust-bot/trust-bot-1.0.0-SNAPSHOT/bin/trust-bot
    && chmod 540 /opt/trust-bot/trust-bot-1.0.0-SNAPSHOT/bin/trust-bot

USER 1001

ENTRYPOINT [ "/opt/trust-bot/trust-bot-1.0.0-SNAPSHOT/bin/trust-bot" ]
