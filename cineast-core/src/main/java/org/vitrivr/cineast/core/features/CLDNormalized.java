package org.vitrivr.cineast.core.features;

import org.vitrivr.cineast.core.config.ReadableQueryConfig;
import org.vitrivr.cineast.core.data.FloatVector;
import org.vitrivr.cineast.core.data.ReadableFloatVector;
import org.vitrivr.cineast.core.data.frames.VideoFrame;
import org.vitrivr.cineast.core.data.score.ScoreElement;
import org.vitrivr.cineast.core.data.segments.SegmentContainer;
import org.vitrivr.cineast.core.features.abstracts.AbstractFeatureModule;
import org.vitrivr.cineast.core.util.ColorLayoutDescriptor;
import org.vitrivr.cineast.core.util.ImageHistogramEqualizer;

import java.util.List;

public class CLDNormalized extends AbstractFeatureModule {

  public CLDNormalized() {
    super("features_CLDNormalized", 1960f / 4f, 12);
  }

  @Override
  public void processSegment(SegmentContainer shot) {
    if (shot.getMostRepresentativeFrame() == VideoFrame.EMPTY_VIDEO_FRAME) {
      return;
    }
    if (!phandler.idExists(shot.getId())) {
      FloatVector fv = ColorLayoutDescriptor.calculateCLD(ImageHistogramEqualizer.getEqualized(shot.getMostRepresentativeFrame().getImage()));
      persist(shot.getId(), fv);
    }
  }

  @Override
  public List<ScoreElement> getSimilar(SegmentContainer sc, ReadableQueryConfig qc) {
    FloatVector query = ColorLayoutDescriptor.calculateCLD(ImageHistogramEqualizer.getEqualized(sc.getMostRepresentativeFrame().getImage()));
    return getSimilar(ReadableFloatVector.toArray(query), qc);
  }


}
