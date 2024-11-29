package com.bobathief.UrlRedirector;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UrlData {
    /**
    * Url that will be shortened.
    */
    private String originalUrl;

    /**
    * Expiration data for the url.
    */
    private long expirationDate;
}
