package javax.microedition.m3g;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class TriangleStripArray extends IndexBuffer {
	private int[] indexStrip;
	private IntBuffer buffer;

	public TriangleStripArray(int firstIndex, int[] stripLengths) {
		if (stripLengths == null) {
			throw new NullPointerException();
		} else if (stripLengths.length == 0) {
			throw new IllegalArgumentException();
		}

		int sumStripLengths = 0;

		for (int i = stripLengths.length - 1; i >= 0; --i) {
			if (stripLengths[i] < 3) {
				throw new IllegalArgumentException();
			}

			sumStripLengths += stripLengths[i];
		}

		if (firstIndex + sumStripLengths > 65535) {
			throw new IllegalArgumentException();
		} else {
			super.indices = new int[sumStripLengths];

			for (int i = 0; i < sumStripLengths; ++i) {
				super.indices[i] = firstIndex + i;
			}

			indexStrip = new int[stripLengths.length];
			System.arraycopy(stripLengths, 0, indexStrip, 0, stripLengths.length);
			allocateBuffer(indices.length).put(indices);
		}
	}

	public TriangleStripArray(int[] indices, int[] stripLengths) {
		if (indices == null || stripLengths == null) {
			throw new NullPointerException();
		} else if (stripLengths.length == 0) {
			throw new IllegalArgumentException();
		}

		int sumStripLengths = 0;

		for (int i = stripLengths.length - 1; i >= 0; --i) {
			if (stripLengths[i] < 3) {
				throw new IllegalArgumentException();
			}

			sumStripLengths += stripLengths[i];
		}

		if (indices.length < sumStripLengths) {
			throw new IllegalArgumentException();
		}

		for (int i = sumStripLengths - 1; i >= 0; --i) {
			if (indices[i] < 0 || indices[i] > 65535) {
				throw new IllegalArgumentException();
			}
		}

		super.indices = new int[sumStripLengths];
		System.arraycopy(indices, 0, super.indices, 0, sumStripLengths);
		indexStrip = new int[stripLengths.length];
		System.arraycopy(stripLengths, 0, indexStrip, 0, stripLengths.length);
		allocateBuffer(indices.length).put(indices);
	}

	protected Object3D duplicateObject() {
		TriangleStripArray clone = (TriangleStripArray) super.duplicateObject();
		clone.indices = (int[]) super.indices.clone();
		clone.indexStrip = (int[]) indexStrip.clone();

		return clone;
	}

	public int getStripCount() {
		return indexStrip.length;
	}

	public int[] getIndexStrip(int index) {
		if (index >= 0 && index < indexStrip.length) {
			int sumStripLengths = 0;

			for (int i = 0; i < index; ++i) {
				sumStripLengths += indexStrip[i];
			}

			int[] resIndexStrip = new int[indexStrip[index]];
			if (this.indexStrip != null) {
				System.arraycopy(super.indices, sumStripLengths, resIndexStrip, 0, indexStrip[index]);
			}

			return resIndexStrip;
		} else {
			return null;
		}
	}

	public IntBuffer getBuffer(int index) {
		if (index >= 0 && index < indexStrip.length) {
			int sumStripLengths = 0;

			for (int i = 0; i < index; ++i) {
				sumStripLengths += indexStrip[i];
			}

			buffer.position(sumStripLengths);
			buffer.limit(sumStripLengths + indexStrip[index]);
			return buffer;
		} else {
			return null;
		}
	}

	protected boolean getIndices(int index, int[] indices) {
		int index2 = 0;

		for (int i = 0; i < indexStrip.length; ++i) {
			if (index < indexStrip[i] - 2) {
				indices[0] = super.indices[index2 + index + 0];
				indices[1] = super.indices[index2 + index + 1];
				indices[2] = super.indices[index2 + index + 2];
				indices[3] = index & 1;

				return true;
			}

			index -= indexStrip[i] - 2;
			index2 += indexStrip[i];
		}

		return false;
	}

	private IntBuffer allocateBuffer(int size) {
		buffer = ByteBuffer.allocateDirect((size * 4 / 3) << 2)
				.order(ByteOrder.nativeOrder()).asIntBuffer();
		buffer.position(0);
		return buffer;
	}

	public int profilerCount() {
		return indices.length - indexStrip.length * 2;
	}
}
