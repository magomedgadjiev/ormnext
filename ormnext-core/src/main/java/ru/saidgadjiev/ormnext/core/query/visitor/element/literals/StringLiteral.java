package ru.saidgadjiev.ormnext.core.query.visitor.element.literals;

import ru.saidgadjiev.ormnext.core.query.visitor.QueryVisitor;

/**
 * String literal.
 *
 * @author Said Gadjiev
 */
public class StringLiteral implements Literal<String> {

    /**
     * Current value.
     */
    private final String value;

    /**
     * Create a new instance.
     * @param value target value
     */
    public StringLiteral(String value) {
        this.value = value;
    }

    @Override
    public String getOriginal() {
        return value;
    }

    @Override
    public String get() {
        return value;
    }

    @Override
    public void accept(QueryVisitor visitor) {
        visitor.visit(this);
    }
}
