package com.mirelanita.support;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.WARNING;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita/)
 * @version 1.0, Mar 28, 2017
 */
public class ClosingIterator<E, T extends Iterable<E> & Closeable> implements Iterator<E>, Closeable {

    private boolean closed;

    private final T delegate;

    private final Iterator<E> iterator;

    public ClosingIterator(T delegate) {
        this.delegate = requireNonNull(delegate);
        this.iterator = delegate.iterator();
    }

    public boolean isClosed() { return closed; }

    @Override
    public boolean hasNext() { return !closed && iterator.hasNext(); }

    @Override
    public E next() {
        try {
            final E next = iterator.next();
            if (!iterator.hasNext()) {
                close();
            }
            return next;
        } catch (NoSuchElementException nse) {
            close();
            throw nse;
        }
    }

    @Override
    public void close() {
        try {
            getDelegate().close();
            closed = true;
        } catch (IOException e) {
            handleCloseException(e);
        }
    }

    protected T getDelegate() { return delegate; }

    protected void handleCloseException(IOException closeException) {
        Logger.getLogger(getClass().getName()).log(WARNING, "cannot close iterator", closeException);
    }
}
