package com.video.ui.utils;

public final class StringDecoder {

    private String mSource;
    private int mIdxContentEnd;
    private int mIdxIndex;
    private int mLastContentIndex;

    public StringDecoder begin(String source) {
        mSource = source;
        mLastContentIndex = 0;
        mIdxContentEnd = mSource.lastIndexOf(StringEncoder.CONTENT_SPLIT);
        if (mIdxContentEnd < 0) {
            throw new BadEncodeException("bad format, str=" + source);
        }
        mIdxIndex = mIdxContentEnd;
        return this;
    }

    public StringDecoder end() {
        mSource = null;
        return this;
    }

    public String read() {
        int contentIndex = 0;
        ++mIdxIndex;
        if (mIdxIndex >= mSource.length()) {
            contentIndex = mIdxContentEnd;
        } else {
            int radix = 0;
            while(true) {
                char c = mSource.charAt(mIdxIndex);
                if (c == StringEncoder.INDEX_SPLIT) {
                    break;
                }
                ++mIdxIndex;
                int n = c - '0';
                if (n < 0) {
                    throw new BadEncodeException("bad format, str=" + mSource);
                }

                if (n > 9) {
                    n = c - 'a';
                    if (n < 0 || n > 5) {
                        throw new BadEncodeException("bad format, str=" + mSource);
                    }

                    n += 10;
                }

                contentIndex += n << radix;
                radix += 4;
            }
        }
        String ret = mSource.substring(mLastContentIndex, contentIndex);
        mLastContentIndex = contentIndex;
        return ret;
    }

    public static class BadEncodeException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public BadEncodeException(String msg) {
            super(msg);
        }
    }
}
