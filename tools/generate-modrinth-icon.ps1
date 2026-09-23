Add-Type -AssemblyName System.Drawing

$size = 512
$output = Join-Path $PSScriptRoot '..\assets\modrinth-icon.png'
$bitmap = [System.Drawing.Bitmap]::new($size, $size)
$graphics = [System.Drawing.Graphics]::FromImage($bitmap)
$graphics.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias
$graphics.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic

function New-RoundedPath([float]$x, [float]$y, [float]$width, [float]$height, [float]$radius) {
    $path = [System.Drawing.Drawing2D.GraphicsPath]::new()
    $diameter = $radius * 2
    $path.AddArc($x, $y, $diameter, $diameter, 180, 90)
    $path.AddArc($x + $width - $diameter, $y, $diameter, $diameter, 270, 90)
    $path.AddArc($x + $width - $diameter, $y + $height - $diameter, $diameter, $diameter, 0, 90)
    $path.AddArc($x, $y + $height - $diameter, $diameter, $diameter, 90, 90)
    $path.CloseFigure()
    return $path
}

function Fill-Rounded([System.Drawing.Graphics]$target, [float]$x, [float]$y, [float]$width, [float]$height, [float]$radius, [System.Drawing.Brush]$brush) {
    $path = New-RoundedPath $x $y $width $height $radius
    $target.FillPath($brush, $path)
    $path.Dispose()
}

$graphics.Clear([System.Drawing.Color]::FromArgb(17, 24, 39))
$background = [System.Drawing.Drawing2D.LinearGradientBrush]::new(
    [System.Drawing.Rectangle]::new(16, 16, 480, 480),
    [System.Drawing.Color]::FromArgb(17, 24, 39),
    [System.Drawing.Color]::FromArgb(32, 43, 61),
    45)
Fill-Rounded $graphics 16 16 480 480 112 $background
$background.Dispose()

$border = [System.Drawing.Pen]::new([System.Drawing.Color]::FromArgb(57, 72, 94), 3)
$borderPath = New-RoundedPath 17.5 17.5 477 477 110.5
$graphics.DrawPath($border, $borderPath)
$borderPath.Dispose(); $border.Dispose()

$shadow = [System.Drawing.SolidBrush]::new([System.Drawing.Color]::FromArgb(90, 5, 7, 11))
Fill-Rounded $graphics 104 104 304 304 62 $shadow
$shadow.Dispose()

$tile = [System.Drawing.Drawing2D.LinearGradientBrush]::new(
    [System.Drawing.Rectangle]::new(104, 92, 304, 304),
    [System.Drawing.Color]::FromArgb(255, 208, 120),
    [System.Drawing.Color]::FromArgb(233, 154, 47),
    90)
Fill-Rounded $graphics 104 92 304 304 62 $tile
$tile.Dispose()

$innerPen = [System.Drawing.Pen]::new([System.Drawing.Color]::FromArgb(170, 255, 224, 160), 3)
$innerPath = New-RoundedPath 121 109 270 270 47
$graphics.DrawPath($innerPen, $innerPath)
$innerPath.Dispose(); $innerPen.Dispose()

$monogram = [System.Drawing.SolidBrush]::new([System.Drawing.Color]::FromArgb(23, 27, 37))
$points = [System.Drawing.PointF[]]@(
    [System.Drawing.PointF]::new(181, 154),
    [System.Drawing.PointF]::new(243, 154),
    [System.Drawing.PointF]::new(243, 295),
    [System.Drawing.PointF]::new(345, 295),
    [System.Drawing.PointF]::new(345, 357),
    [System.Drawing.PointF]::new(181, 357)
)
$graphics.FillPolygon($monogram, $points)
$monogram.Dispose()

$dotBrush = [System.Drawing.SolidBrush]::new([System.Drawing.Color]::FromArgb(255, 241, 207))
$graphics.FillEllipse($dotBrush, 357, 357, 16, 16)
$dotBrush.Color = [System.Drawing.Color]::FromArgb(165, 255, 241, 207)
$graphics.FillEllipse($dotBrush, 382, 357, 16, 16)
$dotBrush.Color = [System.Drawing.Color]::FromArgb(90, 255, 241, 207)
$graphics.FillEllipse($dotBrush, 407, 357, 16, 16)
$dotBrush.Dispose()

$graphics.Dispose()
$bitmap.Save($output, [System.Drawing.Imaging.ImageFormat]::Png)
$bitmap.Dispose()
Write-Output "Generated $output"
