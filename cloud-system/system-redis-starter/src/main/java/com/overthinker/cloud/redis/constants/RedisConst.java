package com.overthinker.cloud.redis.constants;

public class RedisConst {

    public static final String ARTICLE_VISIT_COUNT = "article:visit:count:";

    public static final String ARTICLE_LIKE_COUNT = "article:like:count";

    public static final String ARTICLE_COMMENT_COUNT = "article:comment:count";

    public static final String ARTICLE_FAVORITE_COUNT = "article:favorite:count";

    public static final String BLACK_LIST_UID_KEY = "auth:blacklist:uid:";

    public static final String BLACK_LIST_IP_KEY = "auth:blacklist:ip:";

    public static final String EMAIL_VERIFICATION_LINK = "email:verification:link:";

    public static final String VIDEO_VISIT_COUNT = "video:visit:count:";

    public static final String VERIFY_CODE = "email:verify:code:";

    public static final String SEPARATOR = ":";

    public static final Integer VERIFY_CODE_EXPIRATION = 5;

    private RedisConst() {
    }
}