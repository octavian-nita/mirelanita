package com.mirelanita.support;

import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.util.Objects.requireNonNull;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita/)
 * @version 1.0, Mar 28, 2017
 */
public class ClosingIterator<E, T extends Closeable & Iterable<E>> implements Iterator<E> {

    private final T delegate;

    private final Iterator<E> iterator;

    protected void close(T delegate) {
        try {
            delegate.close();
        } catch (IOException e) {
            LoggerFactory.getLogger(ClosingIterator.class).error("cannot close delegate", e);
        }
    }

    public ClosingIterator(T delegate) {
        this.delegate = requireNonNull(delegate);
        this.iterator = delegate.iterator();
    }

    @Override
    public boolean hasNext() { return iterator.hasNext(); }

    @Override
    public E next() {
        try {
            final E next = iterator.next();
            if (!iterator.hasNext()) {
                close(delegate);
            }
            return next;
        } catch (NoSuchElementException nse) {
            close(delegate);
            throw nse;
        }
    }
}
