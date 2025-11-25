package _bg.footballbettingapp.common.model;


import lombok.Getter;

@Getter
public enum Country {

    BULGARIA("BG", "Bulgaria"),
    AUSTRIA("AT", "Austria"),
    GERMANY("DE", "Germany"),
    UNITED_KINGDOM("UK", "United Kingdom"),
    FRANCE("FR", "France"),
    ITALY("IT", "Italy"),
    SPAIN("ES", "Spain"),
    NETHERLANDS("NL", "Netherlands"),
    DENMARK("DK", "Denmark"),
    GREECE("GR", "Greece"),
    HUNGARY("HU", "Hungary"),
    IRELAND("IE", "Ireland"),
    POLAND("PL", "Poland"),
    PORTUGAL("PT", "Portugal"),
    ROMANIA("RO", "Romania"),
    SWEDEN("SE", "Sweden"),
    SWITZERLAND("CH", "Switzerland"),
    SCOTLAND("GB", "Scotland");

    private final String code;
    private final String displayName;

    Country(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }


}

