package com.android.netactivity.net;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapperHC4;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author renmingming
 *
 */
public class PublishMultipartEntity extends HttpEntityWrapperHC4
{

    private final ProgressListener listener;

    public PublishMultipartEntity(final HttpEntity entity,final ProgressListener listener) {
        super(entity);
        this.listener = listener;
    }


    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        super.writeTo(new CountingOutputStream(outstream, this.listener));
    }

    public static interface ProgressListener {
        void transferred(long num);
    }

    public static class CountingOutputStream extends FilterOutputStream {

        private final ProgressListener listener;
        private long transferred;

        public CountingOutputStream(final OutputStream out,
                                    final ProgressListener listener) {
            super(out);
            this.listener = listener;
            this.transferred = 0;
        }

        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            this.transferred += len;
            this.listener.transferred(this.transferred);
        }

        public void write(int b) throws IOException {
            out.write(b);
            this.transferred++;
            this.listener.transferred(this.transferred);
        }
    }

}  
