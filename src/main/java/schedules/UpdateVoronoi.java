package schedules;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import services.Interface.UpdateVoronoiService;

public class UpdateVoronoi {

	private boolean isRunning = false;
	

	private static Logger logger =  LogManager.getLogger(UpdateVoronoi.class);  
	
	//spring依赖注入的时候应该是根据后面的 uvs来的，包括setter/getter方法都是uvs
	//不然前面是是String，不见得在 getString()/setString()
	private UpdateVoronoiService uvs;
	
	public UpdateVoronoiService getUpdateVoronoiService() {
		return uvs;
	}

	public void setUpdateVoronoiService(UpdateVoronoiService uvs) {
		this.uvs = uvs;
	}
	
	protected void execute() {
		if (!isRunning) {
			
			logger.warn("开始自动重构泰森多边形=======================================");
			isRunning=true;
			uvs.updateVoronoi();
			isRunning=false;
			logger.warn("自动重构泰森多边形结束=======================================");
			
		}
	}
}
