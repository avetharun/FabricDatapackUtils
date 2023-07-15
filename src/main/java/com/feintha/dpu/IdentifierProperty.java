package com.feintha.dpu;

import net.minecraft.state.State;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class IdentifierProperty extends Property<Identifier> {
    Identifier thisID;
    protected IdentifierProperty(String name) {
        super(name, Identifier.class);
    }

    public static IdentifierProperty of(String name) {
        return new IdentifierProperty(name);
    }
    @Override
    public Collection<Identifier> getValues() {
        return new ArrayList<>();
    }
    @Override
    public Value<Identifier> createValue(Identifier value) {
        return new Value<>(this, value);
    }

    @Override
    public Value<Identifier> createValue(State<?, ?> state) {
        return new Value<>(this, state.get(this));
    }

    @Override
    public String name(Identifier value) {
        return thisID.toString();
    }

    @Override
    public Optional<Identifier> parse(String name) {
        return Optional.of(new Identifier(name));
    }
    public int computeHashCode() {
        long hash = alib.getHash64(thisID.toUnderscoreSeparatedString());
        int first = (int) (hash << 16);
        int second = (int) (hash);
        return ((first ^ second ) * second) | first;
    }
}
