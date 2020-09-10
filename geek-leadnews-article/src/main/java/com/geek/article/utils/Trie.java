package com.geek.article.utils;

import java.util.ArrayList;
import java.util.List;

public class Trie {

    private TrieNode root;

    public Trie() {
        root = new TrieNode();
        root.var = ' ';
    }

    /**
     * 插入 trie 树。
     *
     * @param word
     */
    public void insert(String word) {
        TrieNode ws = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!ws.children.keySet().contains(c)) {
                ws.children.put(c, new TrieNode(c));
            }
            ws = ws.children.get(c);
        }
        ws.isWord = true;
    }

    /**
     * 查询 trie 树。
     *
     * @param prefix
     * @return
     */
    public List<String> startWith(String prefix) {
        List<String> match = new ArrayList<>();
        TrieNode ws = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (!ws.children.keySet().contains(c)) return match;
            ws = ws.children.get(c);
            if (!ws.containLongTail) {
                for (char cc : ws.children.keySet()) {
                    match.add(prefix + cc);
                }
            } else {
                // 包含长尾词。从 map 中取。
            }
        }
        return match;
    }
}
