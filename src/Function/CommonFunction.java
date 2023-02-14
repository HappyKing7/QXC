package Function;

import Enum.*;

public class CommonFunction {
	public Boolean quanBaoAndKD(String label){
		if(label.contains(TypeEnum.ZL.getLabel()) && label.length()>2){
			if(!label.contains("全包"))
				return true;
		}
		if(label.contains(TypeEnum.ZS.getLabel()) && label.length()>2){
			if(!label.contains("全包"))
				return true;
		}
		if(label.contains(TypeEnum.KD.getLabel()) && label.length()>2){
			return true;
		}
		return false;
	}
}
