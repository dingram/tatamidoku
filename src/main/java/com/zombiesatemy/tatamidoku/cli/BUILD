java_library(
    name = "lib",
    srcs = glob(
        ["*.java"],
        exclude = ["CommandLineClient.java"],
    ),
    deps = [
        "//src/main/java/com/zombiesatemy/tatamidoku/game",
        "@maven//:org_jline_jline",
    ],
)

java_binary(
    name = "cli",
    srcs = [
        "CommandLineClient.java",
    ],
    main_class = "com.zombiesatemy.tatamidoku.cli.CommandLineClient",
    deps = [
        ":lib",
        "//src/main/java/com/zombiesatemy/tatamidoku/game",
        "@maven//:com_beust_jcommander",
        "@maven//:org_fusesource_jansi_jansi",
        "@maven//:org_jline_jline",
    ],
)
