#!/usr/bin/env python3

import png  # PyPNG: https://github.com/drj11/pypng  doc: https://pythonhosted.org/pypng/index.html

png_reader = png.Reader("HoN.png")
width, height, pixels, metadata = png_reader.read()

out_pixels = []
for row in pixels:
    out_row = []
    for pixel_start_index in range(0, len(row), 3):
        r, g, b = row[pixel_start_index:pixel_start_index+3]

        # only use value if odd
        r = r if r % 2 == 1 else 0
        g = g if g % 2 == 1 else 0
        b = b if b % 2 == 1 else 0

        out_row.append(r)
        out_row.append(g)
        out_row.append(b)

    out_pixels.append(out_row)

png_writer = png.Writer(width=width, height=height)
out_file = open("out.png", "wb")
png_writer.write(out_file, out_pixels)
out_file.close()
