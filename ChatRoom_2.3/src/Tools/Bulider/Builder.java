package Tools.Bulider;

import java.io.IOException;

//�����߳�����
public abstract class Builder {

	public Builder(){}
	protected abstract Object getResult() throws IOException;
}
