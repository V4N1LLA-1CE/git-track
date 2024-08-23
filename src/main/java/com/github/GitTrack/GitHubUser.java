package com.github.GitTrack;

import java.time.LocalDateTime;

/**
 * GitHubUser
 */
public record GitHubUser(
    String login,
    int id,
    String nodeId,
    String avatarUrl,
    String gravatarId,
    String url,
    String htmlUrl,
    String followersUrl,
    String followingUrl,
    String gistsUrl,
    String starredUrl,
    String subscriptionsUrl,
    String organizationsUrl,
    String reposUrl,
    String eventsUrl,
    String receivedEventsUrl,
    String type,
    boolean siteAdmin,
    String name,
    String company,
    String blog,
    String location,
    String email,
    Boolean hireable,
    String bio,
    String twitterUsername,
    int publicRepos,
    int publicGists,
    int followers,
    int following,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
