import java.util.Random;

public class Component{
    Type componentType;

    public Component(Type theType){
        this.componentType = theType;
    }

    public Type getComponentType(){
        return componentType;
    }

}