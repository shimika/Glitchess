package nanami.mashiro.glitchess.screen;

public class ScreenFunction {
	public static int EasingFunction(int now, int target, int e) {
		int gap;
		if (now < target) {
			gap = Math.max(2, (target - now) / e);
			gap = Math.min(target, now + gap);
		} else {
			gap = Math.min(-2, (target - now) / e);
			gap = Math.max(target, now + gap);
		}

		return gap;
	}
}
