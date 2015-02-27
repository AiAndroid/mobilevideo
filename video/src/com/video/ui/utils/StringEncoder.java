package com.video.ui.utils;

public final class StringEncoder {
    // ASSERT CONTENT_SPLIT.lenght()==INDEX_SPLIT.lenght();
    static final char CONTENT_SPLIT = '$';
    static final char INDEX_SPLIT = ',';
    static final char[] DIGITALS = new char[] {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    private StringBuilder mDstBuilder;
    private StringBuilder mIdxBuilder;

    public final StringEncoder begin(StringBuilder sb1, StringBuilder sb2) {
        mDstBuilder = sb1;
        mIdxBuilder = sb2;
        return this;
    }

    public final String end() {
        if (mIdxBuilder.length() > 0) {
            mDstBuilder.append(CONTENT_SPLIT);
            mDstBuilder.append(mIdxBuilder);
        }

        String ret = mDstBuilder.toString();
        mDstBuilder = null;
        mIdxBuilder = null;
        return ret;
    }

    public final StringEncoder write(String source) {
        if (source == null) {
            source = "";
        }

        int num = mDstBuilder.length();
        if (num > 0) {
            do {
                mIdxBuilder.append(DIGITALS[num & 0x0F]);
                num >>= 4;
            } while (num > 0);
            mIdxBuilder.append(INDEX_SPLIT);
        }

        mDstBuilder.append(source);
        return this;
    }

}
