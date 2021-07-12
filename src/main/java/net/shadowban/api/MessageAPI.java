package net.shadowban.api;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.time.Instant;

public class MessageAPI {

    private final EmbedBuilder embedBuilder;

    public MessageAPI() {
        this.embedBuilder = new EmbedBuilder();
    }

    public MessageAPI setAuthor(String name, String url, String iconUrl) {
        embedBuilder.setAuthor(name, url, iconUrl);
        return this;
    }

    public MessageAPI setAuthor(String name, String url) {
        embedBuilder.setAuthor(name, url);
        return this;
    }

    public MessageAPI setAuthor(String name) {
        embedBuilder.setAuthor(name);
        return this;
    }

    public MessageAPI addBlankField(boolean inline) {
        embedBuilder.addBlankField(inline);
        return this;
    }

    public MessageAPI addField(String name, String value, boolean inline) {
        embedBuilder.addField(name, value, inline);
        return this;
    }

    public MessageAPI addField(MessageEmbed.Field field) {
        embedBuilder.addField(field);
        return this;
    }

    public MessageAPI appendDescription(CharSequence charSequence) {
        embedBuilder.appendDescription(charSequence);
        return this;
    }

    public MessageAPI setColor(Color color) {
        embedBuilder.setColor(color);
        return this;
    }

    public MessageAPI setDescription(String description) {
        embedBuilder.setDescription(description);
        return this;
    }

    public MessageAPI setFooter(String text) {
        embedBuilder.setFooter(text);
        return this;
    }

    public MessageAPI setFooter(String text, String iconUrl) {
        embedBuilder.setFooter(text, iconUrl);
        return this;
    }

    public MessageAPI setImage(String url) {
        embedBuilder.setImage(url);
        return this;
    }

    public MessageAPI setThumbnail(String url) {
        embedBuilder.setThumbnail(url);
        return this;
    }

    public MessageAPI addTimestamp() {
        embedBuilder.setTimestamp(Instant.now());
        return this;
    }

    public MessageAPI setTitle(String title) {
        embedBuilder.setTitle(title);
        return this;
    }

    public MessageAPI setTitle(String title, String url) {
        embedBuilder.setTitle(title, url);
        return this;
    }

    public EmbedBuilder getEmbedBuilder() {
        return embedBuilder;
    }

    public MessageEmbed build() {
        return embedBuilder.build();
    }

}
