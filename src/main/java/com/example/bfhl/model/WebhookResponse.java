package com.example.bfhl.model;

public class WebhookResponse {
  private String webhook;
  private String accessToken;

  public WebhookResponse(String webhook, String accessToken) {
    this.webhook = webhook;
    this.accessToken = accessToken;
  }
  public String getWebhook() {
    return webhook;
  }
  public String getAccessToken() {
    return accessToken;
  }
  public void setWebhook(String webhook) {
    this.webhook = webhook;
  }
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
  
}
