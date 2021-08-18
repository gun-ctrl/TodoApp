import android.content.Context

/**
 *@Description
 *@Author PC
 *@QQ 1578684787
 */

/**
 * dp值转换为px值
 */
fun dp2x(context: Context,dp: Int):Int{
    return (context.resources.displayMetrics.density*dp).toInt()
}