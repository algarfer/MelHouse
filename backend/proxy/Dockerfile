FROM alpine:3.20.2 AS build-stage

# cURL installation
RUN apk --no-cache add curl

# Download and installation envsubst
RUN curl -L https://github.com/a8m/envsubst/releases/download/v1.2.0/envsubst-Linux-arm64 -o envsubst
RUN chmod +x envsubst
RUN mv envsubst /usr/local/bin

FROM nginx:1.24-alpine-slim

# Copy envsubst binary from build-stage
COPY --from=build-stage /usr/local/bin/envsubst /usr/local/bin/envsubst

# Copy default.conf template
COPY default.conf.template /etc/nginx/conf.d/default.conf.template

# Copy SSL certificates
# COPY certificate.crt /etc/nginx/ssl/certificate.crt
# COPY ca_bundle.crt /etc/nginx/ssl/ca_bundle.crt
# COPY private.key /etc/nginx/ssl/private.key

# Copy robots.txt
# COPY robots.txt /usr/share/nginx/html/robots.txt

# Define build args with the upstream servers
ARG PENPOT_URI=http://localhost:9001

ENV PENPOT_URI=$PENPOT_URI

# Expose ports
EXPOSE 80
# EXPOSE 443

# Upstreams servers URI substitution and nginx execution
CMD envsubst \
    -i /etc/nginx/conf.d/default.conf.template \
    -o /etc/nginx/conf.d/default.conf && \
    nginx -g 'daemon off;'
