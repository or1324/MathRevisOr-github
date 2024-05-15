package or.nevet.orcloud;

import or.nevet.orgeneralhelpers.constants.CloudConstants;

public class RealtimeField {
    private final String fieldLocation;
    private final Object fieldValue;

    private final String lastLocationPathPart;

    public RealtimeField(String location, Object value) {
        fieldLocation = location;
        fieldValue = value;
        String[] pathParts = location.split(CloudConstants.cloudLocationSeparator);
        lastLocationPathPart = pathParts[pathParts.length-1];
    }
    public Object getFieldValue() {
        return fieldValue;
    }

    public String getFieldLocation() {
        return fieldLocation;
    }

    public String getLastLocationPathPart() {
        return lastLocationPathPart;
    }
}
