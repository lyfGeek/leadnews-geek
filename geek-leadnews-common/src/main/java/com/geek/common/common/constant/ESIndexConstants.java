package com.geek.common.common.constant;

public class ESIndexConstants {

    public static final String DEFAULT_DOC = "_doc";
    public static final String ALL_INDEX = "app_info_*";
    public static final String ARTICLE_INDEX = "app_info_article";
    public static final String USER_INDEX = "app_info_user";
    public static final String AUTHOR_INDEX = "app_info_author";

}


/*
PUT app_info_article
{
  "mappings": {
    "_doc": {
      "properties": {
        "channelId": {
          "type": "long"
        },
        "content": {
          "type": "text",
          "fields": {
            "keyword": {
              "type": "keyword",
              "ignore_above": 256
            }
          },
          "analyzer": "ik_smart"
        },
        "id": {
          "type": "long"
        },
        "pub_time": {
          "type": "date"
        },
        "publishTime": {
          "type": "date"
        },
        "query": {
          "properties": {
            "match_all": {
              "type": "object"
            }
          }
        },
        "reason": {
          "type": "text",
          "fields": {
            "keyword": {
              "type": "keyword",
              "ignore_above": 256
            }
          }
        },
        "status": {
          "type": "long"
        },
        "tag": {
          "type": "text",
          "fields": {
            "keyword": {
              "type": "keyword",
              "ignore_above": 256
            }
          }
        },
        "title": {
          "type": "text",
          "fields": {
            "keyword": {
              "type": "keyword",
              "ignore_above": 256
            }
          },
          "analyzer": "ik_smart"
        },
        "userId": {
          "type": "long"
        }
      }
    }
  }
}
 */
