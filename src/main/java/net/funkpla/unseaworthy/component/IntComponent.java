package net.funkpla.unseaworthy.component;


import org.ladysnake.cca.api.v3.component.Component;

public interface IntComponent extends Component {
    int getValue();

    void setValue(int i);
}
