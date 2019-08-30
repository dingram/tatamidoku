java_library(
    name = "lib",
    srcs = glob(
        ["*.java"],
        exclude = ["CommandLineClient.java"],
    ),
)

java_binary(
    name = "cli",
    srcs = [
        "CommandLineClient.java",
    ],
    main_class = "com.zombiesatemy.tatamidoku.cli.CommandLineClient",
    deps = [
        ":lib",
        "@maven//:com_beust_jcommander",
        "@maven//:org_fusesource_jansi_jansi",
        "@maven//:org_jline_jline",
    ],
)
