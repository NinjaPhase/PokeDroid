package pokejava.api.pokemon;

/**
 * Allows for new experience types to be added into the game to allow for slower/faster
 * progression.
 * 
 * @author PoketronHacker
 * @version 1.0
 *
 */
public interface ExperienceType {
	
	/*
	 * Start of the Standard Experience Types Segment.
	 */
	
	public static final ExperienceType ERRATIC = new ExperienceType() {
		
		@Override
		public String getName() {
			return "Erratic";
		}
		
		@Override
		public int getExperienceAt(int level) {
			double experienceToNext = 0.0;
			if(level < 50) {
				experienceToNext = (Math.pow((double)level, 3.0)
						*(100.0-(double)level))/50.0;
			} else if(level >= 50 && level < 68) {
				experienceToNext = (Math.pow((double)level, 3.0)
						*(150.0-(double)level))/100.0;
			} else if(level >= 68 && level < 98) {
				experienceToNext = (Math.pow((double)level, 3.0)
						*(Math.floor((1911.0-(10.0*level))/3.0)))/500.0;
			} else {
				experienceToNext = (Math.pow((double)level, 3.0)
						*(160.0-(double)level))/100.0;
			}
			if(level <= 1) experienceToNext = 0.0;
			return (int)experienceToNext;
		}
	};
	
	public static final ExperienceType FAST = new ExperienceType() {
		
		@Override
		public String getName() {
			return "Fast";
		}
		
		@Override
		public int getExperienceAt(int level) {
			double exp = 0.0;
			exp = ((4*Math.pow(level, 3.0))/5.0);
			if(level <= 1) exp = 0.0;
			return (int)exp;
		}
	};
	
	public static final ExperienceType MEDIUM_FAST = new ExperienceType() {
		
		@Override
		public String getName() {
			return "Medium Fast";
		}
		
		@Override
		public int getExperienceAt(int level) {
			int experienceToNext = (int)(Math.pow((double)level, 3.0));
			if(level <= 1) experienceToNext = 0;
			return experienceToNext;
		}
	};
	
	public static final ExperienceType MEDIUM_SLOW = new ExperienceType() {
		
		@Override
		public String getName() {
			return "Medium Slow";
		}
		
		@Override
		public int getExperienceAt(int level) {
			double exp = 0.0;
			exp = (6.0/5.0)*Math.pow(level, 3.0);
			exp -= (15*Math.pow(level, 2.0));
			exp += (100.0*level);
			exp -= 140;
			if(level <= 1)
				exp = 0.0;
			return (int) exp;
		}
	};
	
	public static final ExperienceType SLOW = new ExperienceType() {
		
		@Override
		public String getName() {
			return "Slow";
		}
		
		@Override
		public int getExperienceAt(int level) {
			double exp = 0.0;
			exp = ((5*Math.pow(level, 3.0)))/4.0;
			if(level <= 1)
				exp = 0.0;
			return (int) exp;
		}
	};
	
	public static final ExperienceType FLUCTUATING = new ExperienceType() {
		
		@Override
		public String getName() {
			return "Fluctuating";
		}
		
		@Override
		public int getExperienceAt(int level) {
			double exp = 0.0;
			if(level < 15) {
				exp = Math.pow(level, 3.0)*((Math.floor((level+1.0)/3.0)+24.0)/50.0);
			} else if(level >= 15 && level < 36) {
				exp = Math.pow(level, 3.0)*((level+14.0)/5.0);
			} else {
				exp = Math.pow(level, 3.0)*((Math.floor(level/2.0)+32.0)/50.0);
			}
			if(level <= 1)
				exp = 0.0;
			return (int)exp;
		}
	};
	
	/**
	 * @return The name of this experience type of pool storage.
	 */
	public String getName();
	
	/**
	 * @param level The level that the experience is at.
	 * @return The low boundary of the experience at that level.
	 */
	public int getExperienceAt(int level);
	
}
